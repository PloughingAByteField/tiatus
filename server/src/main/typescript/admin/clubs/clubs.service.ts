import { Injectable } from '@angular/core';

import { Club } from '../../clubs/club.model';
import { ClubsService } from '../../clubs/clubs.service';
import { AdminClubsHttpService } from './clubs-http.service';

@Injectable()
export class AdminClubsService extends ClubsService {

    constructor(protected service: AdminClubsHttpService) {
        super(service);
    }

    public createClub(club: Club): Promise<Club> {
        return new Promise((resolve) => this.service.createClub(club).then((c: Club) => {
            this.clubs.push(c);
            this.subject.next(this.clubs);
            resolve(c);
        }));
    }

    public removeClub(club: Club): void {
        this.service.removeClub(club).then((c: Club) => {
            let index = this.clubs.indexOf(c);
            this.clubs.splice(index, 1);
            this.subject.next(this.clubs);
        });
    }

    public updateClub(club: Club): void {
        this.service.updateClub(club).then((c: Club) => {
            let updatedClub = this.getClubForId(c.id);
            if (updatedClub !== null) {
                updatedClub.clubName = c.clubName;
            }
            this.subject.next(this.clubs);
        });
    }

    public getClubForId(clubId: number): Club {
        for (let club of this.clubs) {
            if (club.id === clubId) {
                return club;
            }
        }
        return null;
    }
}
