import React, { useEffect, useMemo, useState } from 'react'
import { createApi } from '../lib/api.js'
import { useAuth } from '../lib/auth.jsx'

function formatDate(iso) {
  try { return new Date(iso).toLocaleString('fr-FR') } catch { return iso }
}

export function OrdersPage() {
  const { token, email } = useAuth()
  const api = useMemo(() => createApi(token), [token])

  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    (async () => {
      try {
        setLoading(true)
        const res = await api.get('/api/me/orders')
        setOrders(res.data)
      } catch (e) {
        setError(e?.response?.data?.error || e.message)
      } finally {
        setLoading(false)
      }
    })()
  }, [])

  return (
    <div>
      <div className="sectionhead">
        <h2>Mes commandes</h2>
        <div className="muted">Connecté en tant que <b>{email}</b></div>
      </div>

      {error && <div className="toast toast-danger">Erreur: {error}</div>}

      {loading ? (
        <div className="muted">Chargement…</div>
      ) : orders.length === 0 ? (
        <div className="card">
          <div className="muted">Aucune commande pour le moment. Retourne au catalogue et passe une commande.</div>
        </div>
      ) : (
        <div className="stack">
          {orders.map(o => (
            <div key={o.id} className="card">
              <div className="orderrow">
                <div>
                  <div className="orderid">Commande #{o.id}</div>
                  <div className="muted">Créée le {formatDate(o.createdAt)}</div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <div className="pill pill-green">{o.status}</div>
                  <div className="muted">Total: {(o.totalCents/100).toFixed(2)} €</div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
