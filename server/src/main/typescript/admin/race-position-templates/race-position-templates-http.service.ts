import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { RacePositionTemplateEntry, convertJsonToRacePositionTemplateEntry }
    from './race-position-template-entry.model';

@Injectable()
export class RacePositionTemplatesHttpService {

    public entries: RacePositionTemplateEntry[];

    private endPoint: string = '/rest/racePositionTemplates/entry';
    private headers = new Headers({ 'Content-Type': 'application/json' });

    constructor(private http: Http) {}

    public createTemplateEntry(entry: RacePositionTemplateEntry):
        Promise<RacePositionTemplateEntry> {
        return this.http
            .post(this.endPoint,
            JSON.stringify(entry), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    let location: string = res.headers.get('location');
                    let locationParts = location.split('/');
                    let id: number = +locationParts[locationParts.length - 1];
                    entry.id = id;
                }
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }

    public removeTemplateEntry(entry: RacePositionTemplateEntry):
        Promise<RacePositionTemplateEntry> {
        return this.http
            .delete(this.endPoint + '/template/' + entry.template + '/position/' + entry.position)
            .toPromise()
            .then(() => {
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateTemplateEntry(entry: RacePositionTemplateEntry):
        Promise<RacePositionTemplateEntry> {
        return this.http
            .put(this.endPoint,
            JSON.stringify(entry), { headers: this.headers })
            .toPromise()
            .then(() => {
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }
}
