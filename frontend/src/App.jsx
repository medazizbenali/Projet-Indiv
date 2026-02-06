import React, { useEffect, useMemo, useState } from 'react'

function basicAuthHeader(username, password) {
  const token = btoa(`${username}:${password}`)
  return `Basic ${token}`
}

export default function App() {
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [username, setUsername] = useState('aziz')
  const [password, setPassword] = useState('aziz123')
  const [cart, setCart] = useState({})
  const [msg, setMsg] = useState('')
  const [orders, setOrders] = useState([])

  const totalItems = useMemo(() => Object.values(cart).reduce((a, b) => a + b, 0), [cart])

  useEffect(() => {
    (async () => {
      try {
        setLoading(true)
        const res = await fetch('/api/products')
        const data = await res.json()
        if (!res.ok) throw new Error(data?.error || `HTTP ${res.status}`)
        setProducts(data)
      } catch (e) {
        setError(String(e))
      } finally {
        setLoading(false)
      }
    })()
  }, [])

  const inc = (id) => setCart(prev => ({ ...prev, [id]: (prev[id] || 0) + 1 }))
  const dec = (id) => setCart(prev => {
    const next = { ...prev }
    const v = (next[id] || 0) - 1
    if (v <= 0) delete next[id]
    else next[id] = v
    return next
  })

  async function checkout() {
    setMsg('')
    const items = Object.entries(cart).map(([productId, quantity]) => ({ productId: Number(productId), quantity: Number(quantity) }))
    if (items.length === 0) { setMsg('Panier vide.'); return }

    const res = await fetch('/api/orders', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': basicAuthHeader(username, password) },
      body: JSON.stringify({ items })
    })
    const data = await res.json().catch(() => ({}))
    if (!res.ok) { setMsg(`Erreur checkout: ${data.error || res.status}`); return }

    setCart({})
    setMsg(`Commande créée: #${data.orderId} (total: ${data.totalCents} cents)`)
  }

  async function loadOrders() {
    setMsg('')
    const res = await fetch('/api/me/orders', { headers: { 'Authorization': basicAuthHeader(username, password) } })
    const data = await res.json().catch(() => [])
    if (!res.ok) { setMsg(`Erreur historique: ${data.error || res.status}`); setOrders([]); return }
    setOrders(data)
  }

  return (
    <div style={{ fontFamily: 'system-ui, -apple-system, Segoe UI, Roboto, Arial', padding: 24, maxWidth: 1000, margin: '0 auto' }}>
      <h1 style={{ marginBottom: 6 }}>Projet Indiv</h1>
      <div style={{ opacity: 0.7, marginBottom: 18 }}>
        Front (Docker) • Swagger: <a href="/swagger-ui/index.html" target="_blank" rel="noreferrer">ouvrir</a>
      </div>

      <section style={{ border: '1px solid #eee', borderRadius: 12, padding: 16, marginBottom: 16 }}>
        <h2 style={{ marginTop: 0 }}>Connexion (Basic Auth)</h2>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap' }}>
          <label>Username<br/><input value={username} onChange={e => setUsername(e.target.value)} /></label>
          <label>Password<br/><input type="password" value={password} onChange={e => setPassword(e.target.value)} /></label>
          <button onClick={loadOrders} style={{ height: 34, alignSelf: 'end' }}>Charger historique</button>
        </div>
        {msg && <div style={{ marginTop: 10 }}>{msg}</div>}
      </section>

      <section style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: 16 }}>
        <div style={{ border: '1px solid #eee', borderRadius: 12, padding: 16 }}>
          <h2 style={{ marginTop: 0 }}>Catalogue</h2>
          {loading && <div>Chargement...</div>}
          {error && <div style={{ color: 'crimson' }}>Erreur: {error}</div>}
          {!loading && !error && (
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ textAlign: 'left' }}>
                  <th>Produit</th>
                  <th>Prix</th>
                  <th>Stock</th>
                  <th>Panier</th>
                </tr>
              </thead>
              <tbody>
                {products.map(p => (
                  <tr key={p.id} style={{ borderTop: '1px solid #f0f0f0' }}>
                    <td>{p.name}</td>
                    <td>{p.priceCents} cents</td>
                    <td>{p.stock}</td>
                    <td>
                      <button onClick={() => dec(p.id)} disabled={!cart[p.id]}>-</button>
                      <span style={{ display: 'inline-block', minWidth: 28, textAlign: 'center' }}>{cart[p.id] || 0}</span>
                      <button onClick={() => inc(p.id)}>+</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
          <div style={{ marginTop: 14, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <div><b>Articles:</b> {totalItems}</div>
            <button onClick={checkout} disabled={totalItems === 0} style={{ padding: '8px 12px' }}>Acheter</button>
          </div>
        </div>

        <div style={{ border: '1px solid #eee', borderRadius: 12, padding: 16 }}>
          <h2 style={{ marginTop: 0 }}>Mon historique</h2>
          {orders.length === 0 ? <div style={{ opacity: 0.7 }}>Clique “Charger historique”.</div> : (
            <ul>{orders.map(o => <li key={o.id}>#{o.id} — {o.status} — {o.totalCents} cents</li>)}</ul>
          )}
        </div>
      </section>
    </div>
  )
}
