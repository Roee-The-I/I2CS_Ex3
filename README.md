# I2CS_Ex3
מטלה רביעית במבוא לחישוב משחק פאקמן 
# Ex3 – Pac-Man Algorithm

## Overview
This project implements a Pac-Man algorithm as part of **Ex3** in the Intro2CS course.
The solution focuses on object-oriented design, code reuse, and integration with the provided game engine.

The implementation follows the required stages:
1. Designing a Pac-Man movement algorithm.
2. Implementing the Pac-Man client-side module.
3. Using the given server-side game engine without modification.

---

## Main Features
- Uses Map / Map2D utilities for distance calculations.
- Evaluates possible moves using a scoring function.
- Avoids walls and dangerous areas (ghost proximity).
- Supports cyclic and non-cyclic maps.
- Prevents infinite back-and-forth movement.
- Fully compatible with the provided `PacmanGame` API.

---

## Project Structure
- **MyEx3Class.java** – Main Pac-Man algorithm (client-side).
- **Ex3Algo.java** – Base algorithm class provided in the assignment.
- **Map / Map2D** – Grid and distance calculation utilities.
- **Ex3Main.java** – Game launcher (provided).

---

## How It Works
At each step, Pac-Man:
1. Reads the current game state from `PacmanGame`.
2. Evaluates all legal neighboring moves.
3. Assigns a score based on safety, distance, and continuity.
4. Selects the best direction according to the evaluation.

---

## Notes
- No server-side code was modified.
- No manual input or GUI interaction is used.
- Debug prints were removed before submission.
- The algorithm is deterministic and runs automatically.

---

## Author
Roee Gil
