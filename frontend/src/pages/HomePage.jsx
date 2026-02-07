import React, { useEffect, useMemo, useState } from 'react'
import { createApi } from '../lib/api.js'
import { useAuth } from '../lib/auth.jsx'

function formatPriceCents(cents) {
  return (cents / 100).toLocaleString('fr-FR', { style: 'currency', currency: 'EUR' })
}

export function HomePage() {
  const { token } = useAuth()
  const api = useMemo(() => createApi(token), [token])

  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [q, setQ] = useState('')
  const [cart, setCart] = useState({})
  const [toast, setToast] = useState('')

  useEffect(() => {
    (async () => {
      try {
        setLoading(true)
        const res = await api.get('/api/products')
        setProducts(res.data)
      } catch (e) {
        setError(e?.response?.data?.error || e.message)
      } finally {
        setLoading(false)
      }
    })()
  }, [])

  const filtered = useMemo(() => {
    const s = q.trim().toLowerCase()
    if (!s) return products
    return products.filter(p => String(p.name).toLowerCase().includes(s))
  }, [products, q])

  const totalItems = useMemo(() => Object.values(cart).reduce((a, b) => a + b, 0), [cart])
  const totalCents = useMemo(() => {
    const byId = new Map(products.map(p => [p.id, p]))
    let total = 0
    for (const [id, qty] of Object.entries(cart)) {
      const p = byId.get(Number(id))
      if (p) total += p.priceCents * qty
    }
    return total
  }, [cart, products])

  function inc(id) { setCart(prev => ({ ...prev, [id]: (prev[id] || 0) + 1 })) }
  function dec(id) {
    setCart(prev => {
      const next = { ...prev }
      const v = (next[id] || 0) - 1
      if (v <= 0) delete next[id]
      else next[id] = v
      return next
    })
  }

  async function checkout() {
    setToast('')
    if (!token) { setToast('Connecte-toi pour passer commande (JWT requis).'); return }
    const items = Object.entries(cart).map(([productId, quantity]) => ({ productId: Number(productId), quantity }))
    if (items.length === 0) { setToast('Ton panier est vide.'); return }

    try {
      const res = await api.post('/api/orders', { items })
      setCart({})
      setToast(`Commande créée: #${res.data.orderId} • Total: ${formatPriceCents(res.data.totalCents)}`)
    } catch (e) {
      setToast(e?.response?.data?.error || e.message)
    }
  }

  return (
    <div>
      <section className="hero">
        <div>
          <div className="kicker">Démo Jury • Auth JWT + DB + Rate Limit</div>
          <h1>Un vrai mini site e-commerce, pas un “Hello World”.</h1>
          <p className="muted">Catalogue public, commande protégée, historique “Mes commandes”, et sécurité (mots de passe hashés, JWT, limitation brute-force).</p>
          <div className="searchrow">
            <input className="input" placeholder="Rechercher un produit…" value={q} onChange={e => setQ(e.target.value)} />
            <button className="btn btn-primary" onClick={checkout}>Passer commande</button>
          </div>
          {toast && <div className="toast">{toast}</div>}
        </div>
        <div className="card" style={{ alignSelf: 'stretch' }}>
          <div className="cardtitle">Panier</div>
          <div className="cartline"><span>Articles</span><b>{totalItems}</b></div>
          <div className="cartline"><span>Total</span><b>{formatPriceCents(totalCents)}</b></div>
          <div className="muted" style={{ marginTop: 8, fontSize: 13 }}>
            Astuce démo : crée un compte, connecte-toi, puis commande.
          </div>
        </div>
      </section>

      <section className="section">
        <div className="sectionhead">
          <h2>Catalogue</h2>
          <div className="muted">{loading ? 'Chargement…' : `${filtered.length} produit(s)`}</div>
        </div>

        {error && <div className="toast toast-danger">Erreur: {error}</div>}

        <div className="grid">
          {filtered.map(p => (
            <div key={p.id} className="product">
              <div className="producttop">
                <div>
                  <div className="productname">{p.name}</div>
                  <div className="muted">Stock: {p.stock}</div>
                </div>
                <div className="price">{formatPriceCents(p.priceCents)}</div>
              </div>

              <div className="qtyrow">
                <button className="btn" onClick={() => dec(p.id)} disabled={!cart[p.id]}>-</button>
                <div className="qty">{cart[p.id] || 0}</div>
                <button className="btn" onClick={() => inc(p.id)}>+</button>
              </div>

              <button className="btn btn-primary" onClick={() => { inc(p.id); setToast('Ajouté au panier ✅') }}>Ajouter au panier</button>
            </div>
          ))}
        </div>
      </section>
    </div>
  )
}
