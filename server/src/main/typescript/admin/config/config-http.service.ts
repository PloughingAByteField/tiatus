import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';

import { ConfigHttpService } from '../../config/config-http.service';

@Injectable()
export class AdminConfigHttpService extends ConfigHttpService {
    private headers = new Headers({ 'Content-Type': 'application/json' });

    private restEndPoint: string = '/rest/config';

    constructor(protected http: Http) {
        super(http);
    }

    public uploadFooter(footer: string): Promise<string> {
        return this.http
            .post(this.restEndPoint + '/footer',
            JSON.stringify(footer), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                return footer;
            })
            .catch((err) => Promise.reject(err));
    }

    public uploadTitle(title: string): Promise<string> {
        return this.http
            .post(this.restEndPoint + '/title',
            JSON.stringify(title), { headers: this.headers })
            .toPromise()
            .then((res: Response) => {
                return title;
            })
            .catch((err) => Promise.reject(err));
    }

    public uploadLogo(file: File): Promise<string> {
        const formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http
            .post(this.restEndPoint + '/logo', formData)
            .toPromise()
            .then((res: Response) => {
                return res.text();
            })
            .catch((err) => Promise.reject(err));
    }
}
