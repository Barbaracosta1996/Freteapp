import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPerfilAnexos } from '../perfil-anexos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../perfil-anexos.test-samples';

import { PerfilAnexosService, RestPerfilAnexos } from './perfil-anexos.service';

const requireRestSample: RestPerfilAnexos = {
  ...sampleWithRequiredData,
  dataPostagem: sampleWithRequiredData.dataPostagem?.format(DATE_FORMAT),
};

describe('PerfilAnexos Service', () => {
  let service: PerfilAnexosService;
  let httpMock: HttpTestingController;
  let expectedResult: IPerfilAnexos | IPerfilAnexos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PerfilAnexosService);
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

    it('should create a PerfilAnexos', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const perfilAnexos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(perfilAnexos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PerfilAnexos', () => {
      const perfilAnexos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(perfilAnexos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PerfilAnexos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PerfilAnexos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PerfilAnexos', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPerfilAnexosToCollectionIfMissing', () => {
      it('should add a PerfilAnexos to an empty array', () => {
        const perfilAnexos: IPerfilAnexos = sampleWithRequiredData;
        expectedResult = service.addPerfilAnexosToCollectionIfMissing([], perfilAnexos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(perfilAnexos);
      });

      it('should not add a PerfilAnexos to an array that contains it', () => {
        const perfilAnexos: IPerfilAnexos = sampleWithRequiredData;
        const perfilAnexosCollection: IPerfilAnexos[] = [
          {
            ...perfilAnexos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPerfilAnexosToCollectionIfMissing(perfilAnexosCollection, perfilAnexos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PerfilAnexos to an array that doesn't contain it", () => {
        const perfilAnexos: IPerfilAnexos = sampleWithRequiredData;
        const perfilAnexosCollection: IPerfilAnexos[] = [sampleWithPartialData];
        expectedResult = service.addPerfilAnexosToCollectionIfMissing(perfilAnexosCollection, perfilAnexos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(perfilAnexos);
      });

      it('should add only unique PerfilAnexos to an array', () => {
        const perfilAnexosArray: IPerfilAnexos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const perfilAnexosCollection: IPerfilAnexos[] = [sampleWithRequiredData];
        expectedResult = service.addPerfilAnexosToCollectionIfMissing(perfilAnexosCollection, ...perfilAnexosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const perfilAnexos: IPerfilAnexos = sampleWithRequiredData;
        const perfilAnexos2: IPerfilAnexos = sampleWithPartialData;
        expectedResult = service.addPerfilAnexosToCollectionIfMissing([], perfilAnexos, perfilAnexos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(perfilAnexos);
        expect(expectedResult).toContain(perfilAnexos2);
      });

      it('should accept null and undefined values', () => {
        const perfilAnexos: IPerfilAnexos = sampleWithRequiredData;
        expectedResult = service.addPerfilAnexosToCollectionIfMissing([], null, perfilAnexos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(perfilAnexos);
      });

      it('should return initial array if no PerfilAnexos is added', () => {
        const perfilAnexosCollection: IPerfilAnexos[] = [sampleWithRequiredData];
        expectedResult = service.addPerfilAnexosToCollectionIfMissing(perfilAnexosCollection, undefined, null);
        expect(expectedResult).toEqual(perfilAnexosCollection);
      });
    });

    describe('comparePerfilAnexos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePerfilAnexos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePerfilAnexos(entity1, entity2);
        const compareResult2 = service.comparePerfilAnexos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePerfilAnexos(entity1, entity2);
        const compareResult2 = service.comparePerfilAnexos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePerfilAnexos(entity1, entity2);
        const compareResult2 = service.comparePerfilAnexos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
