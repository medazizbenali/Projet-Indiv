import React, { createContext, useContext, useEffect, useMemo, useState } from 'react'

const AuthCtx = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('jwt') || '')
  const [email, setEmail] = useState(() => localStorage.getItem('email') || '')

  useEffect(() => {
    if (token) localStorage.setItem('jwt', token)
    else localStorage.removeItem('jwt')
  }, [token])

  useEffect(() => {
    if (email) localStorage.setItem('email', email)
    else localStorage.removeItem('email')
  }, [email])

  const value = useMemo(() => ({ token, setToken, email, setEmail, logout: () => { setToken(''); setEmail('') } }), [token, email])
  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthCtx)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
