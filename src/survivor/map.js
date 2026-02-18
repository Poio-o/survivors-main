import { GameConfig } from "./config.js";
import { Hunter, Prey, Obstacle } from "./entities.js";
import { distance, randInt } from "./utils.js";

const MOVES = [
  [-1, 0],
  [1, 0],
  [0, -1],
  [0, 1],
  [-1, -1],
  [-1, 1],
  [1, -1],
  [1, 1],
];

export class GameMap {
  constructor() {
    this.hunters = [];
    this.preys = [];
    this.obstacles = [];
    this.finished = false;
    this.winner = null;
    this.init();
  }

  init() {
    this.hunters = [];
    this.preys = [];
    this.obstacles = [];
    this.finished = false;
    this.winner = null;

    for (let i = 0; i < GameConfig.INITIAL_OBSTACLES; i++)
      this.obstacles.push(this.randomObstacle());

    for (let i = 0; i < GameConfig.INITIAL_HUNTERS; i++)
      this.hunters.push(this.randomHunter());

    for (let i = 0; i < GameConfig.INITIAL_PREYS; i++)
      this.preys.push(this.randomPrey());
  }

  randomFreeCell() {
    while (true) {
      const x = randInt(1, GameConfig.WIDTH - 2);
      const y = randInt(1, GameConfig.HEIGHT - 2);
      if (!this.isBlocked(x, y)) return { x, y };
    }
  }

  randomHunter() {
    const p = this.randomFreeCell();
    return new Hunter(p.x, p.y);
  }

  randomPrey() {
    const p = this.randomFreeCell();
    return new Prey(p.x, p.y);
  }

  randomObstacle() {
    const p = this.randomFreeCell();
    return new Obstacle(p.x, p.y);
  }

  isBlocked(x, y) {
    if (
      x <= 0 ||
      y <= 0 ||
      x >= GameConfig.WIDTH - 1 ||
      y >= GameConfig.HEIGHT - 1
    )
      return true;

    for (let o of this.obstacles) if (o.x === x && o.y === y) return true;

    for (let h of this.hunters) if (h.x === x && h.y === y) return true;

    for (let p of this.preys) if (p.x === x && p.y === y) return true;

    return false;
  }

  // 🔴 MOVIMIENTO GREEDY COMO EN JAVA
  bestMove(mover, tx, ty, flee) {
    let bestX = mover.x;
    let bestY = mover.y;
    let bestScore = flee ? -Infinity : Infinity;
    let found = false;

    for (let m of MOVES) {
      const nx = mover.x + m[0];
      const ny = mover.y + m[1];

      if (this.isBlocked(nx, ny)) continue;

      const d = distance(nx, ny, tx, ty);

      if ((!flee && d < bestScore) || (flee && d > bestScore)) {
        bestScore = d;
        bestX = nx;
        bestY = ny;
        found = true;
      } else if (d === bestScore && Math.random() < 0.5) {
        bestX = nx;
        bestY = ny;
      }
    }

    if (!found) return null;
    return { x: bestX, y: bestY };
  }

  update() {
    if (this.finished) return;

    // 🟥 HUNTERS persiguen
    for (let h of this.hunters) {
      if (this.preys.length === 0) break;

      let closest = null;
      let minDist = Infinity;

      for (let p of this.preys) {
        const d = distance(h.x, h.y, p.x, p.y);
        if (d < minDist) {
          minDist = d;
          closest = p;
        }
      }

      if (!closest) continue;

      const move = this.bestMove(h, closest.x, closest.y, false);
      if (move) {
        h.x = move.x;
        h.y = move.y;
      }
    }

    // ⚔️ COMBATE — SOLO ORTOGONAL (NO DIAGONAL)
    for (let i = this.preys.length - 1; i >= 0; i--) {
      const p = this.preys[i];

      for (let h of this.hunters) {
        const dx = Math.abs(h.x - p.x);
        const dy = Math.abs(h.y - p.y);

        // SOLO contacto ortogonal
        if ((dx === 1 && dy === 0) || (dx === 0 && dy === 1)) {
          const total = h.vitality + p.vitality;
          if (Math.random() * total < h.vitality) {
            this.preys.splice(i, 1);
          } else {
            this.hunters.splice(this.hunters.indexOf(h), 1);
          }
          break;
        }
      }
    }

    // 🟩 PREYS huyen SOLO si hunter ≤ 5
    for (let p of this.preys) {
      let closest = null;
      let minDist = Infinity;

      for (let h of this.hunters) {
        const d = distance(p.x, p.y, h.x, h.y);
        if (d < minDist) {
          minDist = d;
          closest = h;
        }
      }

      if (!closest || minDist > 5) continue;

      const move = this.bestMove(p, closest.x, closest.y, true);
      if (move) {
        p.x = move.x;
        p.y = move.y;
      }
    }

    // 🏁 FIN
    if (this.preys.length === 0) {
      this.finished = true;
      this.winner = "HUNTERS";
    }
    if (this.hunters.length === 0) {
      this.finished = true;
      this.winner = "PREYS";
    }
  }

  draw(ctx) {
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

    for (let o of this.obstacles) this.drawCell(ctx, o);
    for (let h of this.hunters) this.drawCell(ctx, h);
    for (let p of this.preys) this.drawCell(ctx, p);

    if (this.finished) {
      ctx.fillStyle = this.winner === "HUNTERS" ? "red" : "green";
      ctx.font = "40px monospace";
      ctx.fillText(`${this.winner} WIN`, 50, 100);
    }
  }

  drawCell(ctx, e) {
    ctx.fillStyle = e.color;
    ctx.fillRect(
      e.x * GameConfig.CELL,
      e.y * GameConfig.CELL,
      GameConfig.CELL,
      GameConfig.CELL,
    );
  }
}
