import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { TimesHttpService } from '../../times/times-http.service';

@Injectable()
export class TimesFullHttpService extends TimesHttpService {

  constructor(protected http: HttpClient) {
    super(http);
  }

}
