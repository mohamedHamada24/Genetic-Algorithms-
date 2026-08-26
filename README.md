# Genetic Algorithm for HP Protein Folding Model

This repository implements a **Genetic Algorithm (GA)** in Java to solve the **2D Hydrophobic-Polar (HP) Protein Folding Problem**. The main objective is to find an optimal 2D spatial arrangement for a given amino acid target sequence that maximizes **Hydrophobic-Hydrophobic (H-H) contacts** while completely eliminating self-intersections (overlaps).

---

## 📌 Problem Formulation

* **Genome / Chromosome:** A string of relative or absolute directional steps (`N`, `S`, `E`, `W`).
* **Fixed Input:** Biological sequence of amino acids represented as binary values (`1` for Hydrophobic / H, `0` for Polar / P).
* **Phenotype:** 2D grid coordinates resulting from following the direction vector.
* **Fitness Function:** Measures non-covalent H-H contact counts while applying heavy penalties for grid overlaps.

---

## 🚀 Lab Milestones (Termin 1 – Termin 4)

### **Termin 1: Spatial Folding**
* Initial spatial grid mapping of sequence paths.
* Calculation of initial overlaps and H-H contact statistics.
* Basic visual rendering of candidate folding paths.

### **Termin 2 Beta: Basic Evolutionary Operators  & Random Generation**
* Implementation of **Fitness-Proportional Selection (Roulette Wheel)**.
* * Generation of purely random candidate direction vectors.
* **Single-Point Crossover** operator for combining directional tails between selected parents.
* **Point Mutation** using a fixed, static mutation rate per gene.
* Creation of early generational loops and CSV log export.

### **Termin 3: Standard Generational Pipeline**
* Refinement of the core generational replacement loop.
* Parameter testing across initial benchmark sequences (`SEQ20`, `SEQ36`).
* Logging average and max generation fitness tracking.

### **Termin 4: Advanced GA & Dynamic Mutation**
* **Tournament Selection ($k=5$):** Alternate selection strategy providing higher selection pressure and preventing premature local convergence.
* **Dynamic Mutation Rate Decay (Linear Cooling):**
  * High initial mutation rate ($\approx 15\%$) for early search space **exploration**.
  * Low final mutation rate ($\approx 1\%$) for fine-grained **exploitation** near termination.
* Interactive console toggle between Tournament and Roulette Wheel selection.
* Execution time measurement in milliseconds (`System.currentTimeMillis()`).
* Extended CSV logging with a mandatory `MutationRate` data column.

---

## 🛠️ Project Structure

```text
GA/
├── Termin1/                 #Fitness calculations 
├── src/
│   │   ├── Faltung.java  
├── Termin2beta/             # # Basic GA operators (Static mutation & Roulette Wheel)
├── src/
│   │   ├── Examples.java
│   │   ├── Faltung.java
│   │   ├── FitnessCalculator.java
│   │   ├── MainTermin2.java
│   │   ├── PopulationGenerator.java
│   │   ├── Selection.java
│   │   └── Visualization.java
│   ├── ga_log_termin2.csv
│   └── best_fold_termin2.png
├── Termin3/                 # Generational loop & benchmark logger
├── src/
│   │   ├── Crossover.java
│   │   ├── Examples.java
│   │   ├── Faltung.java
│   │   ├── FitnessCalculator.java
│   │   ├── MainTermin3.java
│   │   ├── Mutation.java
│   │   ├── PopulationGenerator.java
│   │   ├── Selection.java
│   │   └── Visualization.java
│   ├── ga_log_termin3.csv
│   └── best_fold_termin3.png
├── Termin4/                 # Advanced GA (Tournament Selection & Dynamic Mutation)
│   ├── src/
│   │   ├── Crossover.java
│   │   ├── Examples.java
│   │   ├── Faltung.java
│   │   ├── FitnessCalculator.java
│   │   ├── MainTermin4.java
│   │   ├── Mutation.java
│   │   ├── PopulationGenerator.java
│   │   ├── Selection.java
│   │   └── Visualization.java
│   ├── ga_log_termin4.csv
│   └── best_fold_termin4.png
└── README.md
