import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Race } from '../../races/race.model';
import { RacesHttpService } from '../../races/races-http.service';

@Injectable()
export class AdminRacesHttpService extends RacesHttpService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(protected http: HttpClient) {
        super(http);
    }

    public createRace(race: Race): Promise<Race> {
        return this.http
            .post(this.endPoint,
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
            .delete(this.endPoint + '/' + race.id)
            .toPromise()
            .then(() => {
                return race;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateRace(race: Race): Promise<Race> {
        return this.http
            .put(this.endPoint + '/' + race.id,
            JSON.stringify(race), { headers: this.headers })
            .toPromise()
            .then(() => {
                return race;
            })
            .catch((err) => Promise.reject(err));
    }
}
