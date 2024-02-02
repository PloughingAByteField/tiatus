import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Position } from '../../positions/position.model';
import { PositionsHttpService } from '../../positions/positions-http.service';

@Injectable()
export class AdminPositionsHttpService extends PositionsHttpService {
    private httpHeader = {
        observe: 'response' as const,
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    constructor(protected http: HttpClient) {
        super(http);
    }

    public createPosition(position: Position): Promise<Position> {
        return this.http
            .post(this.endPoint,
                position, 
                this.httpHeader)
            .toPromise()
            .then((res: HttpResponse<Position>) => {
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
            .delete(this.endPoint + '/' + position.id)
            .toPromise()
            .then(() => {
                return position;
            })
            .catch((err) => Promise.reject(err));
    }

    public updatePosition(position: Position): Promise<Position> {
        return this.http
            .put(this.endPoint+ '/' + position.id,
                position, 
                this.httpHeader)
            .toPromise()
            .then(() => {
                return position;
            })
            .catch((err) => Promise.reject(err));
    }
}
