import {serveInitialBoard} from "./ServeInitialBoard.js";
import {activatePiece} from "./MovePiece.js";
import {serveBoard} from "./LoadBoard.js";
import {endGame} from "./EndGame.js";

window.onload = function() {
  localStorage.activateFirstPiece = false;
  localStorage.activateSecondPiece = false;
  serveInitialBoard();
  activatePiece();
  serveBoard();
  endGame();
}