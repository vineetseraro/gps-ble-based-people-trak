import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import { HttpRestService } from '../../../core/http-rest.service';
import { Gadget, GadgetModel } from './gadget.model';
import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Rx';
import { environment } from '../../../../environments/environment';

@Injectable()
export class GadgetService {

  serviceUrl = 'dashboard' + environment.serverEnv;

  constructor(private akRestService: HttpRestService) { }

  /**
   * Get All Record Lists
   */
  getAll(): Observable<GadgetModel> {
    return this.akRestService.get(this.serviceUrl + '/gadgets/available')
      .map((res:any) => res.json());
  }

  /**
   * Read the collection api
   * @param id
   */
  get(id:any) {
    return this.akRestService.get(this.serviceUrl + '/gadgets/available/' + id)
      .map((res:any) => res.json());
  }

  /**
   * Save Gadget Services
   * @param request
   */
  add(request:any) {
    return this.akRestService.post(this.serviceUrl + '/gadgets/available', request, 'multipart/form-data;')
      .map((res:any) => res.json());
  }

  /**
   * Update the collection
   * @param request
   * @param id
   */
  update(request:any , id:any) {
    return this.akRestService.put(this.serviceUrl + '/gadgets/available/' + id, request)
      .map((res:any) => <Gadget>res.json());
  }

  /**
   * Remove Gadget
   * @param request
   */
  remove(request:any) {
    return this.akRestService.post(this.serviceUrl + '/gadgets/available', request)
      .map((res:any) => res.json());
  }
}
