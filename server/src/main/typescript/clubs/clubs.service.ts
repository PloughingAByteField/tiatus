import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Club, convertObjectToClub } from './club.model';
import { ClubsHttpService } from './clubs-http.service';

import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';

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

    public processMessage(message: Message): void {
        console.log('process message');
        console.log(message);
        let club: Club = convertObjectToClub(message.data);
        console.log(club);
        if (message.type === MessageType.ADD) {
            this.clubs.push(club);

        } else if (message.type === MessageType.DELETE) {
            let deletedClub: Club = this.getClubForId(club.id);
            if (deletedClub !== null) {
                let index = this.clubs.indexOf(deletedClub);
                let sliced = this.clubs.splice(index, 1);
            }

        } else if (message.type === MessageType.UPDATE) {
            let updatedClub: Club = this.getClubForId(club.id);
            if (updatedClub !== null) {
                updatedClub.clubName = club.clubName;
            }
        }

        this.subject.next(this.clubs);
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
