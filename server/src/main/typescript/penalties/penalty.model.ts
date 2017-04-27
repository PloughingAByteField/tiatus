export class Penalty {
    public comment: string;
    public note: string;
    public time: number;
    public entry: number;
    public id: number;

    constructor() {
        this.comment = '';
        this.note = '';
    }
}

export function convertObjectToPenalty(obj: any): Penalty {
    const penalty: Penalty = new Penalty();
    penalty.comment = obj.comment;
    penalty.note = obj.note;
    penalty.time = obj.time;
    penalty.entry = obj.entry;
    penalty.id = obj.id;
    return penalty;
}
