# Tents And Trees Solver
A Java program that takes a Tents And Trees puzzle as input and outputs a solution if possible.

## General Info
The program can solve any sized Tents And Trees puzzle. Check out [Brain Bashers](https://www.brainbashers.com/tents.asp) if you'd like to read the game rules or have a go at solving some puzzles yourself. 
The puzzle is solved by using a depth first search algorithm. It is optimised by only attempting to place tents next to trees and by checking that no game rules are broken by placing down a tent.
The program will either output the solution to the puzzle, print 'No solution' or print the error arrising from the given input.

## Input Format
The puzzle can be inputed using stdin, using a .txt file is recomended. Examples are provided in the testFiles folder.
The .txt file must be a rectangular grid consisting of either T (trees) or . (empty space).
There will then be two lines of space separated numbers. The first line will be the required number of tents in each row (from bottom to top).
The second line will be the required number of tents in each column (from left to right).

## Legend
* T - Tree
* C - Campsite/Tent
* . - Empty space
