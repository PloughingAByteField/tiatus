import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Club } from '../../clubs/club.model';
import { ClubsHttpService } from '../../clubs/clubs-http.service';

@Injectable()
export class AdminClubsHttpService extends ClubsHttpService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(protected http: HttpClient) {
        super(http);
    }

   public createClub(club: Club): Promise<Club> {
        return this.http
            .post(this.endPoint,
            JSON.stringify(club), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    club.id = id;
                }
                return club;
            })
            .catch((err) => Promise.reject(err));
    }

    public removeClub(club: Club): Promise<Club> {
        return this.http
            .delete(this.endPoint + '/' + club.id)
            .toPromise()
            .then(() => {
                return club;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateClub(club: Club): Promise<Club> {
        return this.http
            .put(this.endPoint + '/' + club.id,
            JSON.stringify(club), { headers: this.headers })
            .toPromise()
            .then(() => {
                return club;
            })
            .catch((err) => Promise.reject(err));
    }
}
