import React, { useMemo, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { createApi } from '../lib/api.js'
import { useAuth } from '../lib/auth.jsx'

export function RegisterPage() {
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
      const res = await api.post('/api/auth/register', { email, password })
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
        <h2>Inscription</h2>
        <p className="muted">Mot de passe hashé (BCrypt) en base • JWT en réponse.</p>

        {error && <div className="toast toast-danger">{error}</div>}

        <form onSubmit={onSubmit} className="form">
          <label className="label">Email</label>
          <input className="input" value={email} onChange={e => setEmailInput(e.target.value)} placeholder="toi@exemple.com" required />

          <label className="label">Mot de passe</label>
          <input className="input" type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="min 8 caractères" required minLength={8} />

          <button className="btn btn-primary" disabled={loading}>{loading ? 'Création…' : 'Créer mon compte'}</button>
        </form>

        <div className="muted" style={{ marginTop: 10 }}>
          Déjà un compte ? <Link to="/login">Se connecter</Link>
        </div>
      </div>
    </div>
  )
}
