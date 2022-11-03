import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IWhatsMessageBatch } from '../whats-message-batch.model';
import { WhatsMessageBatchService } from '../service/whats-message-batch.service';

import { WhatsMessageBatchRoutingResolveService } from './whats-message-batch-routing-resolve.service';

describe('WhatsMessageBatch routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WhatsMessageBatchRoutingResolveService;
  let service: WhatsMessageBatchService;
  let resultWhatsMessageBatch: IWhatsMessageBatch | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(WhatsMessageBatchRoutingResolveService);
    service = TestBed.inject(WhatsMessageBatchService);
    resultWhatsMessageBatch = undefined;
  });

  describe('resolve', () => {
    it('should return IWhatsMessageBatch returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWhatsMessageBatch = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWhatsMessageBatch).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWhatsMessageBatch = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWhatsMessageBatch).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IWhatsMessageBatch>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWhatsMessageBatch = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWhatsMessageBatch).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
