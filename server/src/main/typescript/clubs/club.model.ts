export class Club {
    public clubName: string;
    public id: number;
}

export function convertObjectToClub(json: Club): Club {
    let club: Club = new Club();
    club.clubName = json.clubName;
    club.id = json.id;
    return club;
}
