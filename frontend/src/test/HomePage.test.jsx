import React from 'react'
import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import { HomePage } from '../pages/HomePage.jsx'

// On mocke l'API pour ne pas dépendre du backend pendant les tests front.
vi.mock('../lib/api.js', () => ({
  createApi: () => ({
    get: async () => ({
      data: [
        { id: 1, name: 'Perceuse', priceCents: 1999 },
        { id: 2, name: 'Marteau', priceCents: 599 }
      ]
    }),
    post: async () => ({ data: { orderId: 1, totalCents: 1999 } })
  })
}))

// On mocke l'auth (pas besoin de token pour charger le catalogue)
vi.mock('../lib/auth.jsx', () => ({
  useAuth: () => ({ token: null })
}))

describe('HomePage', () => {
  it('affiche le catalogue après chargement', async () => {
    render(<HomePage />)

    // le titre est immédiat
    expect(screen.getByRole('heading', { name: /Catalogue/i })).toBeInTheDocument()


    // un produit apparaît après le fetch mocké
    expect(await screen.findByText('Perceuse')).toBeInTheDocument()
    expect(screen.getByText('Marteau')).toBeInTheDocument()
  })
})
