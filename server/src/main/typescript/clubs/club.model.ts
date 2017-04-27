export class Club {
    public clubName: string;
    public id: number;
}

export function convertObjectToClub(obj: any): Club {
    const club: Club = new Club();
    club.clubName = obj.clubName;
    club.id = obj.id;
    return club;
}
