import { randInt } from "./utils.js";

export class Entity {
    constructor(x, y, color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.vitality = randInt(1, 10);
    }
}

export class Hunter extends Entity {
    constructor(x, y) {
        super(x, y, "red");
    }
}

export class Prey extends Entity {
    constructor(x, y) {
        super(x, y, "green");
    }
}

export class Obstacle {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.color = "brown";
    }
}
