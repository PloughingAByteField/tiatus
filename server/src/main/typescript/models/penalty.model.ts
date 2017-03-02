export class Penalty {
    public comment: string;
    public note: string;
    public time: number;
    public entry: number;
    public id: number;
}

export function convertJsonToPenalty(json: Penalty): Penalty {
    let penalty: Penalty = new Penalty();
    penalty.comment = json.comment;
    penalty.note = json.note;
    penalty.time = json.time;
    penalty.entry = json.entry;
    penalty.id = json.id;
    return penalty;
}
