# Backlog (V1) — Fonction métier : achat / commande / historique

> Objectif : répondre au besoin "vente en ligne" et "suivi des achats" du contexte La Petite Maison de l'Épouvante.

## US-01 — Consulter le catalogue (Public)
**En tant que** visiteur
**Je veux** voir la liste des objets disponibles
**Afin de** choisir un produit à acheter.

**Critères d'acceptation**
- L'endpoint `GET /api/products` renvoie une liste de produits (id, nom, prix, stock).
- Accessible sans authentification.
- Si la base est vide, le service renvoie une liste vide.

## US-02 — Créer un compte et se connecter (Pré-requis sécurité)
**En tant que** client
**Je veux** créer un compte puis me connecter
**Afin de** pouvoir passer commande.

**Critères d'acceptation**
- `POST /api/auth/register` crée un utilisateur (email unique) et renvoie un JWT.
- `POST /api/auth/login` renvoie un JWT si le mot de passe est correct.
- Le mot de passe est stocké hashé (BCrypt).

## US-03 — Passer une commande (Protégé)
**En tant que** client authentifié
**Je veux** passer une commande avec une liste d'articles
**Afin de** acheter un ou plusieurs produits.

**Critères d'acceptation**
- `POST /api/orders` requiert un JWT valide.
- La commande contient au moins 1 ligne avec quantité > 0.
- Si un produit n'existe pas : erreur explicite.
- Si stock insuffisant : erreur explicite.
- Le total est calculé en cents (prix * quantité) et stock décrémenté.
- La réponse renvoie `orderId`, `status`, `totalCents`.

## US-04 — Consulter mon historique de commandes (Protégé)
**En tant que** client authentifié
**Je veux** consulter l'historique de mes commandes
**Afin de** suivre mes achats.

**Critères d'acceptation**
- `GET /api/me/orders` requiert un JWT valide.
- La liste renvoie les commandes de l'utilisateur (id, date, statut, total).

## Notes "qualité" (non-fonctionnel)
- **Tests** :
  - tests d'intégration (MockMvc) sur les flux principaux,
  - tests unitaires (service layer) sur le calcul de commande.
- **Observabilité** : Actuator expose `health`, `metrics`, `prometheus`.
- **Sécurité** : rate limiting sur `/api/auth/*`, JWT, routes publiques/privées.