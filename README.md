# Hop! — jeu de plateformes en Java

Jeu de plateformes vertical développé avec Java Swing. Le joueur doit progresser de plateforme en plateforme tout en évitant la lave, récupérer des pièces et s’adapter à une difficulté croissante.

## Fonctionnalités

- interface graphique construite avec Swing ;
- génération dynamique des plateformes ;
- plateformes standards, oscillantes, temporaires et trampolines ;
- lave ascendante et difficulté progressive ;
- pièces donnant des sauts supplémentaires ;
- niveaux, score courant et historique des scores ;
- écrans de menu, de jeu et de classement ;
- musique et ressources graphiques optionnelles.

## Structure du dépôt

```text
.
├── res/                     # Emplacement des ressources graphiques et audio
├── src/                     # Code source Java
│   ├── Hop.java             # Point d’entrée et boucle principale
│   ├── Axel.java            # Personnage et déplacements
│   ├── Field.java           # Terrain, plateformes et difficulté
│   ├── GamePanel.java       # Rendu et commandes
│   └── ...
├── .gitignore
├── Makefile
└── README.md
```

## Prérequis

- JDK 8 ou version ultérieure ;
- `make` pour utiliser les commandes proposées.

## Compilation et lancement

```bash
make
make run
```

Sans `make`, l’équivalent est :

```bash
mkdir -p build/classes
javac -d build/classes src/*.java
java -cp build/classes Hop
```

## Commandes

| Touche | Action |
|---|---|
| Flèche gauche / droite | Déplacement horizontal |
| Flèche haut | Saut |
| Flèche bas | Descente rapide |
| Espace | Utiliser un saut supplémentaire |

## Ressources

Le jeu prévoit les fichiers optionnels suivants dans `res/` :

- `luigi25.png` et `mario.png` ;
- `volcan1.jpg` et `volcan2.jpg` ;
- `son_hop.wav`.

Ces ressources n’étaient pas incluses dans l’archive d’origine. Le code conserve des rendus de remplacement lorsque les images sont absentes ; le son est simplement désactivé.

## Contexte

Projet universitaire réalisé en binôme dans le cadre de la Licence Informatique à l’Université Paris-Saclay.
