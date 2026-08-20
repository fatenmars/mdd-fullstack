# MDD — Monde de Dév

MDD est un réseau social dédié aux développeurs, qui leur permet de s'abonner à des thèmes de programmation, de suivre un fil d'actualité, et de publier des articles et des commentaires. L'application est construite en full-stack, avec un front-end **Angular** et un back-end **Spring Boot** qui communiquent via une API REST sécurisée par **JWT**.

## Stack technique

| Couche | Technologies |
|---|---|
| Front-end | Angular 19, TypeScript, SCSS |
| Back-end | Spring Boot 3.4, Java 21, Spring Security (JWT), Spring Data JPA |
| Base de données | MySQL 8 (via Docker) |
| Tests | JUnit 5 / Mockito / JaCoCo (back), Karma / Jasmine (front) |

## Structure du dépôt

- `front/` — application Angular
- `back/` — API Spring Boot
- `docker-compose.yml` — base de données MySQL

## Prérequis

- **Java 21**
- **Node.js** 20+ et npm
- **Docker** et Docker Compose
- Maven (le wrapper `./mvnw` est inclus, rien à installer)

## Configuration

À la racine du dépôt, crée un fichier **`.env`** pour la base de données (utilisé par `docker-compose.yml`) :

```env
MYSQL_ROOT_PASSWORD=un_mot_de_passe_root
MYSQL_DATABASE=mdd
MYSQL_USER=mdd_user
MYSQL_PASSWORD=un_mot_de_passe_applicatif
```

Le **back-end** a besoin, comme variables d'environnement, des identifiants de la base et du secret JWT :

| Variable | Description |
|---|---|
| `MYSQL_USER` | utilisateur MySQL de l'application (`mdd_user`) |
| `MYSQL_PASSWORD` | mot de passe de cet utilisateur |
| `JWT_SECRET` | secret de signature des tokens JWT (au moins 32 caractères) |

## Installation et lancement

**1. Base de données** (depuis la racine du dépôt) :
```bash
docker compose up -d
```

**2. Back-end** (depuis `back/`) — démarre sur `http://localhost:8080` :
```bash
./mvnw spring-boot:run
```

**3. Front-end** (depuis `front/`) — démarre sur `http://localhost:4200` :
```bash
npm install
npm start
```

## API — endpoints

Toutes les routes hors `/auth/**` nécessitent un token JWT dans l'en-tête `Authorization: Bearer <token>`.

| Endpoint | Méthode | Description |
|---|---|---|
| `/auth/register` | POST | Inscrit un utilisateur, renvoie un token JWT |
| `/auth/login` | POST | Authentifie (email **ou** username), renvoie un token JWT |
| `/users/me` | GET | Détail du profil de l'utilisateur courant |
| `/users/me` | PUT | Met à jour le profil (email, username, mot de passe) |
| `/themes` | GET | Liste des thèmes (avec l'état d'abonnement) |
| `/users/me/themes/{id}` | POST | S'abonne à un thème |
| `/users/me/themes/{id}` | DELETE | Se désabonne d'un thème |
| `/articles` | GET | Fil d'actualité (paramètre `order=asc\|desc`) |
| `/articles` | POST | Crée un article |
| `/articles/{id}` | GET | Détail d'un article et ses commentaires |
| `/articles/{id}/comments` | POST | Ajoute un commentaire à un article |

La déconnexion se fait côté client (suppression du token) : avec un JWT *stateless*, le serveur ne conserve aucune session.

## Tests

**Back-end** (depuis `back/`) — exécute les tests et génère le rapport de couverture JaCoCo :
```bash
./mvnw test
```
Rapport : `back/target/site/jacoco/index.html`

**Front-end** (depuis `front/`) — exécute les tests et génère le rapport de couverture :
```bash
npm test -- --watch=false --code-coverage
```
Rapport : `front/coverage/front/index.html`
