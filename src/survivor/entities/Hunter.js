import Entity from "./Entity.js";

export default class Hunter extends Entity {
    constructor(x, y) {
        super(x, y);
        this.color = "red";
    }
}
