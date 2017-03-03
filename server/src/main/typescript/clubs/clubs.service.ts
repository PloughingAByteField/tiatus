import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Club } from './club.model';
import { ClubsHttpService } from './clubs-http.service';

@Injectable()
export class ClubsService {
    protected clubs: Club[] = new Array<Club>();
    protected subject: BehaviorSubject<Club[]>
        = new BehaviorSubject<Club[]>(this.clubs);

    constructor(protected service: ClubsHttpService) {
        this.refresh();
    }

    public getClubs(): BehaviorSubject<Club[]> {
        return this.subject;
    }

    public refresh(): void {
        this.service.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
            this.subject.next(this.clubs);
        });
    }
}
