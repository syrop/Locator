package pl.org.seva.locator.domain.cleanarchitecture.exception

class UnknownDomainException(throwable: Throwable) : DomainException(throwable) {
    constructor(errorMessage: String) : this(Throwable(errorMessage))
}
