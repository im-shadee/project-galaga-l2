# Project Galaga

## A Java game inspired by Galaga written for my Object-Oriented-Programming class' final project in Year 2 of Computer Science.

<img width="375" height="375" alt="image" src="https://github.com/user-attachments/assets/8498e86e-1a5a-4c81-ba0c-a9cf2a0a97ac" />

<img width="250" height="300" alt="image" src="https://github.com/user-attachments/assets/a4d25eeb-ab07-48ca-b3eb-fd80f0a61098" />

<img width="282" height="316" alt="image" src="https://github.com/user-attachments/assets/5b910a7f-3b7a-418c-b7a5-bbee92d16da8" />

<img width="250" height="300" alt="image" src="https://github.com/user-attachments/assets/dff7964c-278a-4bae-a133-b497f5487fb2" />


## 💭​ How to play?
### User Interface
The screen is split between 3 sections, including:
- A score section at the top of the window displaying the current score and the best score ever,
- A game section where the different actors such as the player, the enemies as well as their respective projectiles evolve and move,
- An information section at the bottom of the window displaying the player's lives, represented by the number of player ships at the bottom-left hand corner, as well as the current level, represented by the number of boost icons at the bottom-right hand corner.

### Player Controls
| Key | Action |
|-----|--------|
| Arrow keys | Move the player |
| Space | Shoot |
| I | Toggle debug mode |
| P | Skip level |

> DEBUG MODE displays the colliders of each actor (enemies, player, projectiles...). **Press the 'I' key on your keyboard to toggle DEBUG MODE.**

> Levels can be skipped by pressing the 'P' key on the keyboard, which allows for easy testing for the reviewers.

## 🛠️​ Technical Details
This project's architecture is separated in two pieces:
- The engine's code
- The game's code

<details>
<summary>Engine</summary>
The 'engine' holds entry-point of the code. It initializes all required singletons (UIManager, GameManager, DebugManager) which then automatically handle starting the game concurrently.

It also exposes many utility classes such as the 'Sprite' class which is represented by a matrix of colors. The sprite API was made modular to make them as easy to work with as possible; for instance, you can replace a sprite's colors at runtime (if a boss loses half of its health for instance) without changing it on disk, and you can even resize the sprite at load time.

It also contains another important class which is DVector2 -- my own Vector2 of doubles (I did not bother making it generic, as this game only works with doubles anyways) specifically tailored for this game. It implements basic Vector2 operations such as normalize, add, etc., as well as a squaredDist method, allowing to compare distances more efficiently -- without computing a square root.
</details>

<details>
<summary>Game</summary>
This section details Game-relevant scripts specifically.

The game architecture was built with a generic game-engine architecture in mind, where concerns are well separated.

There are two main managers, implemented following a Singleton pattern - inspired by Unity's singleton patterns - responsible for making the game work:

GameManager, which:
- Sets up the canvas correctly
- Loads levels
- Communicates data to the UIManager and tells it what to draw
- Handles gameovers/resets

UIManager, which:
- Writes/Reads scores in/from the .sc file when a game starts/end
- Draws and renders the game's actions each frame (player movement, enemy formation/individual movement, bullets...), as well as the game's individual screens, such as the title screen, and level-loading screens.

All actors inherit from the base Entity class, which holds stats and modifiers, a sprite, and implements collision detection (through Rectangle2D's intersects method each game frame). Each child class implement its own movement behavior.

At the time, it was challenging to implement the boss, because the 'Enemy' class' behavior was not detached enough from basic enemy behavior. 
</details>

## 🗒️​ Licensing
Copyright (c) 2026 im-shadee

No permission is granted to use, modify, or redistribute this repository or its contents.
This repository is provided for viewing and portfolio purposes only.

Galaga is property of NAMCO/Midway Games, all rights reserved. This project only serves educational purposes.

## 💭​ Developer's Note
This project was originally intended to be a two-person project. Due to unforeseen circumstances, I ended up completing it on my own under an extremely tight deadline. Despite this, I was able to finish the game and still implement custom behaviour for the boss before the deadline.

This is an older project from when I was still developing my understanding of more advanced OOP concepts, and there are several architectural decisions I would approach differently today.

In particular, the inheritance hierarchy became overly complex on the enemy side as I added increasingly specialized behaviors. I would now separate attack behaviour into interfaces rather than having every enemy inherit from startAtt and updtAtt, since not every enemy — such as the boss — needs to implement the same attack behavior.

I would also separate data such as stats into dedicated record classes rather than passing them through increasingly large constructors.

Despite these limitations, this project was an important learning experience!
