import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Entry } from '../../entries/entry.model';

import { EntriesHttpService } from '../../entries/entries-http.service';
import { FileProcessingResult } from './FileProcessingResult.model';

@Injectable()
export class AdminEntriesHttpService extends EntriesHttpService {

    private httpHeader = {
        observe: 'response' as const,
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    private endPoint: string = '/rest/entries';

    constructor(protected http: HttpClient) {
        super(http);
    }

    public createEntry(entry: Entry): Promise<Entry> {
        return this.http
            .post(this.endPoint,
                entry,
                this.httpHeader)
            .toPromise()
            .then((res: HttpResponse<Entry>) => {
                if (res.status === 201) {
                    const location: string = res.headers.get('location');
                    const locationParts = location.split('/');
                    const id: number = +locationParts[locationParts.length - 1];
                    entry.id = id;
                }
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }

    public removeEntry(entry: Entry): Promise<Entry> {
        return this.http
            .delete(this.endPoint + '/' + entry.id)
            .toPromise()
            .then(() => {
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateEntry(entry: Entry): Promise<Entry> {
        return this.http
            .put(this.endPoint + '/' + entry.id,
                entry,
                this.httpHeader)
            .toPromise()
            .then(() => {
                return entry;
            })
            .catch((err) => Promise.reject(err));
    }

    public updateEntries(entries: Entry[]): Promise<Entry[]> {
        return this.http
            .put(this.endPoint + '/updates',
                entries,
                this.httpHeader)
            .toPromise()
            .then((res) => {
                console.log(res);
                return entries;
            })
            .catch((err) => Promise.reject(err));
    }

    public uploadDraw(file: File): Promise<FileProcessingResult> {
        const formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http
            .post(this.endPoint + '/upload', formData)
            .toPromise()
            .then((res: FileProcessingResult) => {
                return res;
            })
            .catch((err) => {
                console.log(err);
                Promise.reject(err);
                let result: FileProcessingResult = new FileProcessingResult();
                result.code = 500;
                result.data = 'Processing error';
                return result;
        });
    }
}
