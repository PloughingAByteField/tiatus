export class Disqualification {
    public comment: string;
    public note: string;
    public entry: number;
    public id: number;

    constructor() {
        this.comment = '';
        this.note = '';
    }
}

export function convertObjectToDisqualification(obj: any): Disqualification {
    let disqualification: Disqualification = new Disqualification();
    disqualification.comment = obj.comment;
    disqualification.note = obj.note;
    disqualification.entry = obj.entry;
    disqualification.id = obj.id;
    return disqualification;
}
