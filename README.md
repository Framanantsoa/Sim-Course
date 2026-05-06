# 🏁 Simulation de Course

## 📌 Description
Cette application desktop permet de **simuler une course automobile contre le temps** sur une distance de **400 mètres**.

Elle met en évidence plusieurs notions physiques et de performance d’un véhicule.

### 🚗 Paramètres pris en compte
- **Accélération** *(km/h/s)* : vitesse d’augmentation de la vitesse
- **Vitesse maximale** *(km/h)* : limite atteignable par le véhicule
- **Temps d’arrivée** *(s)* : durée pour parcourir 400 m
- **Vitesse finale** *(km/h)* : vitesse au moment de franchir l’arrivée

---

## ⚡ Système de Nitro
L’application propose un système de **boost temporaire (nitro)** permettant d’améliorer les performances :

- **Capacité** *(kg)* : quantité totale de nitro disponible
- **Consommation** *(kg/min)* : vitesse d’utilisation du nitro
- **Boost d’accélération** *(km/h/s²)* : augmentation temporaire de l’accélération

> ⚠️ Le nitro est limité et doit être utilisé stratégiquement.

---

## 🎮 Contrôles
- **S** → Accélérer  
- **N** → Activer le nitro  

---

## 🛠️ Technologies utilisées
- **Affichage** : Java Swing  
- **Logique métier** : Java  
- **Stockage** : Fichiers locaux  

---

## ▶️ Lancement de l’application

### 1. Compilation et exécution
```bash
$ javac -d out/ src/com/race/*.java
$ java -cp out/ com.race.Main
