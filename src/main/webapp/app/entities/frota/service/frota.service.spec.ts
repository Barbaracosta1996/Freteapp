import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFrota } from '../frota.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../frota.test-samples';

import { FrotaService } from './frota.service';

const requireRestSample: IFrota = {
  ...sampleWithRequiredData,
};

describe('Frota Service', () => {
  let service: FrotaService;
  let httpMock: HttpTestingController;
  let expectedResult: IFrota | IFrota[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FrotaService);
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

    it('should create a Frota', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const frota = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(frota).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Frota', () => {
      const frota = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(frota).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Frota', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Frota', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Frota', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFrotaToCollectionIfMissing', () => {
      it('should add a Frota to an empty array', () => {
        const frota: IFrota = sampleWithRequiredData;
        expectedResult = service.addFrotaToCollectionIfMissing([], frota);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frota);
      });

      it('should not add a Frota to an array that contains it', () => {
        const frota: IFrota = sampleWithRequiredData;
        const frotaCollection: IFrota[] = [
          {
            ...frota,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFrotaToCollectionIfMissing(frotaCollection, frota);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Frota to an array that doesn't contain it", () => {
        const frota: IFrota = sampleWithRequiredData;
        const frotaCollection: IFrota[] = [sampleWithPartialData];
        expectedResult = service.addFrotaToCollectionIfMissing(frotaCollection, frota);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frota);
      });

      it('should add only unique Frota to an array', () => {
        const frotaArray: IFrota[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const frotaCollection: IFrota[] = [sampleWithRequiredData];
        expectedResult = service.addFrotaToCollectionIfMissing(frotaCollection, ...frotaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const frota: IFrota = sampleWithRequiredData;
        const frota2: IFrota = sampleWithPartialData;
        expectedResult = service.addFrotaToCollectionIfMissing([], frota, frota2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frota);
        expect(expectedResult).toContain(frota2);
      });

      it('should accept null and undefined values', () => {
        const frota: IFrota = sampleWithRequiredData;
        expectedResult = service.addFrotaToCollectionIfMissing([], null, frota, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frota);
      });

      it('should return initial array if no Frota is added', () => {
        const frotaCollection: IFrota[] = [sampleWithRequiredData];
        expectedResult = service.addFrotaToCollectionIfMissing(frotaCollection, undefined, null);
        expect(expectedResult).toEqual(frotaCollection);
      });
    });

    describe('compareFrota', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFrota(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFrota(entity1, entity2);
        const compareResult2 = service.compareFrota(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFrota(entity1, entity2);
        const compareResult2 = service.compareFrota(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFrota(entity1, entity2);
        const compareResult2 = service.compareFrota(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
