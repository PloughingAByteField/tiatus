export class Position {
    public name: string;
    public active: boolean;
    public showAllEntries: boolean;
    public timing: boolean;
    public canStart: boolean;
    public order: number;
    public id: number;
}

export function convertJsonToPosition(json: Position): Position {
    let position: Position = new Position();
    position.name = json.name;
    position.active = json.active;
    position.showAllEntries = json.showAllEntries;
    position.timing = json.timing;
    position.canStart = json.canStart;
    position.order = json.order;
    position.id = json.id;
    return position;
}
