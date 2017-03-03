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

export function convertJsonToDisqualification(json: Disqualification): Disqualification {
    let disqualification: Disqualification = new Disqualification();
    disqualification.comment = json.comment;
    disqualification.note = json.note;
    disqualification.entry = json.entry;
    disqualification.id = json.id;
    return disqualification;
}
