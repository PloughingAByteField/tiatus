import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { Race } from '../../races/race.model';
import { RacesHttpService } from '../../races/races-http.service';

@Injectable()
export class AdminRacesHttpService extends RacesHttpService {
    private headers = new Headers({ 'Content-Type': 'application/json' });

    constructor(protected http: Http) {
        super(http);
    }

    public createRace(race: Race): Promise<Race> {
        return this.http
            .post(this.endpoint,
            JSON.stringify(race), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    race.id = id;
                }
                return race;
            })
            .catch((err) => Promise.reject(err));
    }

    public removeRace(race: Race): Promise<Race> {
        return this.http
            .delete(this.endpoint + '/' + race.id)
            .toPromise()
            .then(() => {
                return race;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateRace(race: Race): Promise<Race> {
        return this.http
            .put(this.endpoint + '/' + race.id,
            JSON.stringify(race), { headers: this.headers })
            .toPromise()
            .then(() => {
                return race;
            })
            .catch((err) => Promise.reject(err));
    }
}
