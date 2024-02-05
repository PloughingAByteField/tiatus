import { Injectable } from '@angular/core';

import { ClubsService } from '../../clubs/clubs.service';
import { ResultsHttpClubsService } from './clubs-http.service';

import { Club } from '../../clubs/club.model';

@Injectable()
export class ResultsClubsService extends ClubsService {

    constructor(protected service: ResultsHttpClubsService) {
        super(service);
    }

    public refresh(): void {
        this.service.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
            this.subject.next(this.clubs);
        });
    }
}
