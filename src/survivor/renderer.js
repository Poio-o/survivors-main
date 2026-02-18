import { GameConfig } from "./config.js";

export function render(game, ctx) {
    ctx.clearRect(0,0,9999,9999);

    // Walls
    ctx.fillStyle = "white";
    for (let x = 0; x < GameConfig.WIDTH; x++) {
        ctx.fillRect(x*20,0,20,20);
        ctx.fillRect(x*20,(GameConfig.HEIGHT-1)*20,20,20);
    }
    for (let y = 0; y < GameConfig.HEIGHT; y++) {
        ctx.fillRect(0,y*20,20,20);
        ctx.fillRect((GameConfig.WIDTH-1)*20,y*20,20,20);
    }

    for (let o of game.obstacles) {
        ctx.fillStyle = o.color;
        ctx.fillRect(o.x*20,o.y*20,20,20);
    }
    for (let h of game.hunters) {
        ctx.fillStyle = h.color;
        ctx.fillRect(h.x*20,h.y*20,20,20);
    }
    for (let p of game.preys) {
        ctx.fillStyle = p.color;
        ctx.fillRect(p.x*20,p.y*20,20,20);
    }

    if (game.finished) {
        document.getElementById("message").innerText = game.winner;
    }
}
