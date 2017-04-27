import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { Position } from '../../positions/position.model';
import { PositionsHttpService } from '../../positions/positions-http.service';

@Injectable()
export class AdminPositionsHttpService extends PositionsHttpService {
    private headers = new Headers({ 'Content-Type': 'application/json' });

    constructor(protected http: Http) {
        super(http);
    }

    public createPosition(position: Position): Promise<Position> {
        return this.http
            .post(this.endpoint,
            JSON.stringify(position), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    position.id = id;
                }
                return position;
            })
            .catch((err) => Promise.reject(err));
    }

    public removePosition(position: Position): Promise<Position> {
        return this.http
            .delete(this.endpoint + '/' + position.id)
            .toPromise()
            .then(() => {
                return position;
            })
            .catch((err) => Promise.reject(err));
    }

    public updatePosition(position: Position): Promise<Position> {
        return this.http
            .put(this.endpoint,
            JSON.stringify(position), { headers: this.headers })
            .toPromise()
            .then(() => {
                return position;
            })
            .catch((err) => Promise.reject(err));
    }
}
