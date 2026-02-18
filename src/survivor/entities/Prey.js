import Entity from "./Entity.js";

export default class Prey extends Entity {
    constructor(x, y) {
        super(x, y);
        this.color = "lime";
    }
}
