import { GameConfig } from "./config.js";
import { GameMap } from "./map.js";

const canvas = document.getElementById("game");
canvas.width = GameConfig.WIDTH * GameConfig.CELL;
canvas.height = GameConfig.HEIGHT * GameConfig.CELL;

const ctx = canvas.getContext("2d");
let game = new GameMap();
let interval = null;

console.log("main.js loaded");

function start() {
    console.log("START CLICKED");
    if (interval) clearInterval(interval);
    interval = setInterval(() => {
        game.update();
        game.draw(ctx);
    }, GameConfig.TICK_MS);
}

document.getElementById("start").onclick = start;
document.getElementById("restart").onclick = () => {
    game.init();
    start();
};

document.getElementById("options").onclick = () => {
    const p = document.getElementById("optionsPanel");
    p.style.display = p.style.display === "none" ? "block" : "none";
};

document.getElementById("apply").onclick = () => {
    GameConfig.WIDTH = parseInt(document.getElementById("optWidth").value);
    GameConfig.HEIGHT = parseInt(document.getElementById("optHeight").value);
    GameConfig.INITIAL_HUNTERS =
        parseInt(document.getElementById("optHunters").value);
    GameConfig.INITIAL_PREYS =
        parseInt(document.getElementById("optPreys").value);
    GameConfig.INITIAL_OBSTACLES =
        parseInt(document.getElementById("optObstacles").value);
    GameConfig.TICK_MS =
        parseInt(document.getElementById("optSpeed").value);

    canvas.width = GameConfig.WIDTH * GameConfig.CELL;
    canvas.height = GameConfig.HEIGHT * GameConfig.CELL;

    game.init();
    start();
};

