import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICategoriaVeiculo } from '../categoria-veiculo.model';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';

import { CategoriaVeiculoRoutingResolveService } from './categoria-veiculo-routing-resolve.service';

describe('CategoriaVeiculo routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CategoriaVeiculoRoutingResolveService;
  let service: CategoriaVeiculoService;
  let resultCategoriaVeiculo: ICategoriaVeiculo | null | undefined;

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
    routingResolveService = TestBed.inject(CategoriaVeiculoRoutingResolveService);
    service = TestBed.inject(CategoriaVeiculoService);
    resultCategoriaVeiculo = undefined;
  });

  describe('resolve', () => {
    it('should return ICategoriaVeiculo returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaVeiculo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaVeiculo).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaVeiculo = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCategoriaVeiculo).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ICategoriaVeiculo>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaVeiculo = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaVeiculo).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
