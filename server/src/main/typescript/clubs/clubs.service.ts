import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';

import { Club, convertObjectToClub } from './club.model';
import { ClubsHttpService } from './clubs-http.service';
import { Data } from '../model/data.model';

import { Message } from '../websocket/message.model';
import { MessageType } from '../websocket/message-type.model';

@Injectable()
export class ClubsService {
    protected clubs: Club[] = new Array<Club>();
    protected subject: BehaviorSubject<Club[]>
        = new BehaviorSubject<Club[]>(this.clubs);

    protected requested: boolean = false;

    constructor(protected service: ClubsHttpService) {}

    public getClubs(): BehaviorSubject<Club[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.subject;
    }

    public refresh(): void {
        this.service.getClubs().subscribe((data: Data) => {
            if (data.data !== undefined) {
                this.clubs = data.data;
                this.subject.next(this.clubs);
            }
        });
    }

    public processMessage(message: Message): void {
        console.log('process message');
        console.log(message);
        const club: Club = convertObjectToClub(message.data);
        console.log(club);
        if (message.type === MessageType.ADD) {
            this.clubs.push(club);

        } else if (message.type === MessageType.DELETE) {
            const deletedClub: Club = this.getClubForId(club.id);
            if (deletedClub !== null) {
                const index = this.clubs.indexOf(deletedClub);
                const sliced = this.clubs.splice(index, 1);
            }

        } else if (message.type === MessageType.UPDATE) {
            const updatedClub: Club = this.getClubForId(club.id);
            if (updatedClub !== null) {
                updatedClub.clubName = club.clubName;
            }
        }

        this.subject.next(this.clubs);
    }

    public getClubForId(clubId: number): Club {
        if (this.clubs !== undefined) {
            for (const club of this.clubs) {
                if (club.id === clubId) {
                    return club;
                }
            }
        }
        return null;
    }
}
