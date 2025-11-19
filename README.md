# SportApp (Android • Jetpack Compose)

Application Android de news sportives avec authentification (Firebase Auth), favoris hors-ligne (Room), thèmes clair/sombre, et réglages persistés (DataStore). UI 100 % Jetpack Compose.

## ✨ Fonctionnalités

- **Auth** : création de compte, connexion, déconnexion (Firebase Auth Email/Password).
- **Articles** : liste d’articles par section avec filtres en chips, pull-to-refresh, pagination automatique, ouverture en Custom Tab.
- **Favoris** : sauvegarde/suppression locale, affichage hors-ligne (Room).
- **Réglages** :
    - Thème : Système / Clair / Sombre
    - Langue : FR/EN (tag persisté)
    - Section par défaut pour le flux
- **Notifications** : helper de notif locale (+ dépendances FCM prêtes).
- **Préviews Compose** sur plusieurs écrans (pour itérer rapidement dans Android Studio).

## 🧱 Stack technique

- **Langage** : Kotlin (JVM target 11)
- **UI** : Jetpack Compose + Material 3
- **DI** : (simple ViewModel/`viewModel()` — pas de Hilt requis)
- **Navigation** : Navigation Compose
- **Data** : Retrofit + Moshi, Room, DataStore
- **Images** : Coil 3
- **Auth / Cloud** : Firebase (Auth, Firestore, Messaging via BoM)
- **Autres** : WorkManager (dispo), Browser (Custom Tabs)

> `compileSdk=36`, `targetSdk=36`, `minSdk=26`  
> **AGP** 8.13.0, **Kotlin** 2.0.21, **Compose BoM** 2024.10.01, **Firebase BoM** 33.5.1

## 📁 Architecture (vue rapide)

- `ui/` : écrans Compose (`HomeScreen`, `ArticlesScreen`, `FavoritesScreen`, `SettingsScreen`, `LoginScreen`, `RegisterScreen`) + composants réutilisables.
- `navigation/` : `AppNavigation` (TopBar, BottomBar, routes).
- `ui/auth/` : `AuthViewModel` + `RequireAuth` (garde de navigation).
- `data/model/` : modèles (ex. `ArticleDto`).
- `data/repo/` : accès favoris (Room) & data distante (Retrofit).
- `data/settings/` : `SettingsViewModel`, `ThemeMode`, `DataStore`.
- `notifications/` : `NotificationHelper`.

## 🚀 Démarrage rapide

### 1) Prérequis

- Android Studio **Koala+ / Ladybug** (AGP 8.13+)
- JDK 17 (fourni par AS) • Kotlin 2.0.x
- Compte Firebase

### 2) Clonage

```bash
git clone https://github.com/FireFox-d3vFR/SportApp-MobileDev.git
cd SportApp-MobileDev
```

### 3) Firebase

- Crée un projet Firebase et active Authentication (Email/Password).
- Ajoute une appli Android com.example.sportapp.
- Télécharge google-services.json et place-le dans app/.
- (Optionnel) Ajoute SHA-1/256 si tu utilises d’autres providers.

> Le plugin com.google.gms.google-services et la BoM Firebase sont déjà configurés.

### 4) (Optionnel) Clé API pour les articles

Le code prévoit un point d’extension pour une clé (ex. Guardian API). Si tu en ajoutes une :

```kotlin
// app/build.gradle.kts (defaultConfig)
buildConfigField("String", "GUARDIAN_API_KEY", "\"VOTRE_CLE_ICI\"")
```

> Évite de committer la clé : préfère gradle.properties / local.properties ou un secrets.properties ignoré par Git.

### 5) Build & Run

- Depuis Android Studio : Run ▶ app
- CLI :
```
./gradlew :app:assembleDebug
```

## 🔐 Auth & navigation protégée
- `RequireAuth` redirige automatiquement vers **Login** si l’utilisateur n’est pas connecté pour les routes protégées (**Articles, Favorites, Settings**).
- `Settings` inclut un bouton Se déconnecter (**AuthViewModel.logout()**).

## 💾 Données locales
- **Room** pour les favoris (IDs + cache d’articles).
- **DataStore** pour thème, langue, section par défaut.

## 🧪 Prévisualisations Compose

Des `@Preview` existent pour :
- **Home** (TipCard, FeaturedArticleCard…)
- **Settings** (thème clair/sombre)

> Les Previews ne nécessitent pas d’exécuter l’app sur un device.

## 🛠️ Dépendances clés (version catalog)

*(Extrait — voir gradle/libs.versions.toml)*
- Compose BoM `2024.10.01`
- Retrofit `2.11.0` • Moshi `1.15.2`
- OkHttp logging-interceptor `4.12.0`
- Room `2.6.1`
- DataStore `1.1.1`
- Coil 3 `3.0.2`
- Navigation `2.8.3`
- Firebase BoM `33.5.1`