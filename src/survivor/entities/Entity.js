export default class Entity {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.vitality = Math.floor(Math.random() * 10) + 1;
        this.lastX = x;
        this.lastY = y;
    }
}
