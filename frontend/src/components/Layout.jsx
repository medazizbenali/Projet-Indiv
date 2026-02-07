import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { AuthProvider, useAuth } from '../lib/auth.jsx'

function TopNav() {
  const { token, email, logout } = useAuth()
  const nav = useNavigate()

  function onLogout() {
    logout()
    nav('/')
  }

  return (
    <header className="topnav">
      <div className="container navrow">
        <Link to="/" className="brand">CesiShop</Link>

        <div className="navlinks">
          <Link to="/" className="navlink">Catalogue</Link>
          <Link to="/orders" className="navlink">Mes commandes</Link>
          <a className="navlink" href="/swagger-ui/index.html" target="_blank" rel="noreferrer">Swagger</a>
        </div>

        <div className="navauth">
          {token ? (
            <>
              <span className="pill">{email || 'connecté'}</span>
              <button className="btn" onClick={onLogout}>Se déconnecter</button>
            </>
          ) : (
            <>
              <Link className="btn" to="/login">Connexion</Link>
              <Link className="btn btn-primary" to="/register">Inscription</Link>
            </>
          )}
        </div>
      </div>
    </header>
  )
}

export function Layout({ children }) {
  return (
    <AuthProvider>
      <TopNav />
      <main className="container" style={{ paddingTop: 20, paddingBottom: 40 }}>
        {children}
      </main>
      <footer className="footer">
        <div className="container">Projet Indiv • React + Spring Boot • JWT + Rate Limit</div>
      </footer>
    </AuthProvider>
  )
}
