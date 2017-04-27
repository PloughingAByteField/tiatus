export class Position {
    public name: string;
    public id: number;
}

export function convertObjectoPosition(obj: any): Position {
    const position: Position = new Position();
    position.name = obj.name;
    position.id = obj.id;
    return position;
}
