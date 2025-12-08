# ProTube - Sistema d'Autenticació

## 🚀 Com executar l'aplicació

### Backend (Spring Boot)
1. Assegura't que tens Java 17+ instal·lat
2. Des de la carpeta `backend/`, executa:
```bash
mvn spring-boot:run
```
El backend estarà disponible a: `http://localhost:8080`

### Frontend (Next.js)
1. Obre una nova terminal
2. Navega a la carpeta `frontend/`:
```bash
cd frontend
```

3. Instal·la les dependències (només la primera vegada):
```bash
npm install
```

4. Executa l'aplicació:
```bash
npm run dev
```

L'aplicació frontend estarà disponible a: **http://localhost:5173**

## 📱 Funcionalitats implementades

### ✅ Pàgines d'autenticació (en català)
- **Login**: http://localhost:5173/login
- **Registre**: http://localhost:5173/register
- **Home**: http://localhost:5173/

### ✅ Funcionalitats
- ✨ Registre d'usuaris amb validació completa (nom, cognoms, email, username, password)
- 🔐 Login amb username i password
- 🎯 Gestió de sessions amb JWT (token guardat al localStorage)
- 👤 Navbar dinàmica que mostra l'usuari autenticat
- 🚪 Logout des del menú d'usuari
- 📱 Disseny responsive (funciona en mòbil i desktop)
- 🇨🇦 Tot en català

## 🔧 Configuració

### Variables d'entorn
El fitxer `.env.local` ja està configurat amb:
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

Si el teu backend està en un altre port, modifica aquesta variable.

## 📝 Estructura del projecte

```
frontend/
├── app/
│   ├── login/page.tsx          # Pàgina de login
│   ├── register/page.tsx       # Pàgina de registre
│   ├── page.tsx                # Pàgina principal
│   └── providers.tsx           # Providers globals (incloent AuthProvider)
├── components/
│   └── navbar.tsx              # Navbar amb autenticació
├── src/
│   ├── contexts/
│   │   └── AuthContext.tsx     # Context d'autenticació
│   ├── types/
│   │   └── auth.ts             # Tipus TypeScript
│   └── utils/
│       └── authService.ts      # Servei d'autenticació (API calls)
└── .env.local                  # Variables d'entorn
```

## 🎨 Tecnologies utilitzades

- **Framework**: Next.js 16 (App Router)
- **UI Library**: HeroUI (components moderns)
- **Estils**: Tailwind CSS
- **Autenticació**: JWT (JSON Web Tokens)
- **Backend**: Spring Boot + H2 Database

## ⚠️ Notes importants

1. **Ordre d'execució**: Primer executa el backend, després el frontend
2. **Port del frontend**: Configurat per defecte a **5173**
3. **Port del backend**: Espera que estigui a **8080**
4. **Base de dades**: H2 (en memòria, es reinicia quan atures el backend)

## 🐛 Solució de problemes

### El frontend no es connecta al backend
- Verifica que el backend estigui executant-se a `http://localhost:8080`
- Comprova el fitxer `.env.local` i assegura't que `NEXT_PUBLIC_API_URL` sigui correcte

### Error de CORS
- El backend hauria de tenir configurats els CORS per permetre peticions des de `http://localhost:5173`

### No es guarda la sessió
- Comprova la consola del navegador per veure si hi ha errors
- Verifica que el localStorage estigui habilitat al navegador

## 📞 Suport

Si tens problemes, comprova:
1. Que el backend estigui executant-se
2. Que el frontend estigui executant-se al port 5173
3. La consola del navegador per errors JavaScript
4. La consola del terminal per errors del servidor

