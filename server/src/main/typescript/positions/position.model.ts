export class Position {
    public name: string;
    public id: number;
}

export function convertJsonToPosition(json: Position): Position {
    let position: Position = new Position();
    position.name = json.name;
    position.id = json.id;
    return position;
}
