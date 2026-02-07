import React, { useMemo, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { createApi } from '../lib/api.js'
import { useAuth } from '../lib/auth.jsx'

export function LoginPage() {
  const { setToken, setEmail } = useAuth()
  const nav = useNavigate()
  const api = useMemo(() => createApi(''), [])

  const [email, setEmailInput] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function onSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await api.post('/api/auth/login', { email, password })
      setToken(res.data.token)
      setEmail(email)
      nav('/')
    } catch (e2) {
      setError(e2?.response?.data?.error || e2.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="authwrap">
      <div className="authcard">
        <h2>Connexion</h2>
        <p className="muted">Accès aux commandes via JWT.</p>

        {error && <div className="toast toast-danger">{error}</div>}

        <form onSubmit={onSubmit} className="form">
          <label className="label">Email</label>
          <input className="input" value={email} onChange={e => setEmailInput(e.target.value)} placeholder="toi@exemple.com" required />

          <label className="label">Mot de passe</label>
          <input className="input" type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="********" required />

          <button className="btn btn-primary" disabled={loading}>{loading ? 'Connexion…' : 'Se connecter'}</button>
        </form>

        <div className="muted" style={{ marginTop: 10 }}>
          Pas de compte ? <Link to="/register">Créer un compte</Link>
        </div>
      </div>
    </div>
  )
}
