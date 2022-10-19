import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategoriaVeiculo } from '../categoria-veiculo.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../categoria-veiculo.test-samples';

import { CategoriaVeiculoService } from './categoria-veiculo.service';

const requireRestSample: ICategoriaVeiculo = {
  ...sampleWithRequiredData,
};

describe('CategoriaVeiculo Service', () => {
  let service: CategoriaVeiculoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICategoriaVeiculo | ICategoriaVeiculo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CategoriaVeiculoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CategoriaVeiculo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const categoriaVeiculo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(categoriaVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CategoriaVeiculo', () => {
      const categoriaVeiculo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(categoriaVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CategoriaVeiculo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CategoriaVeiculo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CategoriaVeiculo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCategoriaVeiculoToCollectionIfMissing', () => {
      it('should add a CategoriaVeiculo to an empty array', () => {
        const categoriaVeiculo: ICategoriaVeiculo = sampleWithRequiredData;
        expectedResult = service.addCategoriaVeiculoToCollectionIfMissing([], categoriaVeiculo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaVeiculo);
      });

      it('should not add a CategoriaVeiculo to an array that contains it', () => {
        const categoriaVeiculo: ICategoriaVeiculo = sampleWithRequiredData;
        const categoriaVeiculoCollection: ICategoriaVeiculo[] = [
          {
            ...categoriaVeiculo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculoCollection, categoriaVeiculo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CategoriaVeiculo to an array that doesn't contain it", () => {
        const categoriaVeiculo: ICategoriaVeiculo = sampleWithRequiredData;
        const categoriaVeiculoCollection: ICategoriaVeiculo[] = [sampleWithPartialData];
        expectedResult = service.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculoCollection, categoriaVeiculo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaVeiculo);
      });

      it('should add only unique CategoriaVeiculo to an array', () => {
        const categoriaVeiculoArray: ICategoriaVeiculo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const categoriaVeiculoCollection: ICategoriaVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculoCollection, ...categoriaVeiculoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categoriaVeiculo: ICategoriaVeiculo = sampleWithRequiredData;
        const categoriaVeiculo2: ICategoriaVeiculo = sampleWithPartialData;
        expectedResult = service.addCategoriaVeiculoToCollectionIfMissing([], categoriaVeiculo, categoriaVeiculo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaVeiculo);
        expect(expectedResult).toContain(categoriaVeiculo2);
      });

      it('should accept null and undefined values', () => {
        const categoriaVeiculo: ICategoriaVeiculo = sampleWithRequiredData;
        expectedResult = service.addCategoriaVeiculoToCollectionIfMissing([], null, categoriaVeiculo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaVeiculo);
      });

      it('should return initial array if no CategoriaVeiculo is added', () => {
        const categoriaVeiculoCollection: ICategoriaVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addCategoriaVeiculoToCollectionIfMissing(categoriaVeiculoCollection, undefined, null);
        expect(expectedResult).toEqual(categoriaVeiculoCollection);
      });
    });

    describe('compareCategoriaVeiculo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCategoriaVeiculo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCategoriaVeiculo(entity1, entity2);
        const compareResult2 = service.compareCategoriaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCategoriaVeiculo(entity1, entity2);
        const compareResult2 = service.compareCategoriaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCategoriaVeiculo(entity1, entity2);
        const compareResult2 = service.compareCategoriaVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
