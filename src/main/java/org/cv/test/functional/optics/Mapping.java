package org.cv.test.functional.optics;

import io.vavr.collection.Iterator;
import io.vavr.control.Option;

import java.util.List;
import java.util.function.*;

/** example
public enum ContentMapper {
    ;

    private static final String USA_RATING = "USA Rating";

    private static final Mapping<Title, ContentDocument> MAPPING = mapping(

            set(ResourceType.CONTENT, ContentDocument::setResourceType),
            set(ContentType., ContentDocument::setContentType),

            rule(Title::getTitleId, ContentDocument::setResourceId),
            rule(Title::getProgramId, ContentDocument::setProgramId),
            rule(Title::getDuration, rule(Long::intValue, ContentDocument::setDuration)),
            rule(Title::getLicenseStartDate, ContentDocument::setStartTime),
            rule(Title::getLicenseEndDate, ContentDocument::setEndTime),

            rule(Title::getContentProvider, firstOf(ContentProvider::getId),
                    ContentDocument::setChannelId),

            rule(Title::getProgram, flatMap(Program::getGenre).andThen(
                    forAll(Genre::getGenreName)), ContentDocument::setGenres),

            forFirst(Title::getProgram, mapping(
                    rule(Program::getProgramType, rule(ProgramType::map, ContentDocument::setProgramType)),
                    rule(Program::getProgramName, ContentDocument::setTitle),
                    rule(Program::getShortDescription, ContentDocument::setDescription),
                    rule(Program::getParentSeriesId, ContentDocument::setSeriesId),
                    rule(Program::getParentSeasonId, ContentDocument::setSeasonId),
                    rule(Program::getEpisodeTitle, ContentDocument::setEpisodeTitle),
                    rule(Program::getOriginalAirDate, ContentDocument::setOriginalAirDate),
                    rule(Program::getTmsId, ContentDocument::setTmsId),
                    rule(Program::getConnectorId, ContentDocument::setConnectorId),
                    rule(Program::getEpisodeNumber, ContentDocument::setEpisodeNumber),
                    rule(Program::getSeasonNumber, rule(String::valueOf, ContentDocument::setSeasonNumber)),
                    rule(Program::getRating, firstOf(rating -> USA_RATING.equals(rating.getRatingBody()),
                            Rating::getRatingCode), ContentDocument::setParentalRating)
            )).orElse(mapping(
                    rule(Title::getTitle, ContentDocument::setTitle),
                    rule(Title::getProgramType, rule(ProgramType::toString,
                            ContentDocument::setProgramType))
            )),

            rule(Title::getBaselinePolicies, ContentDocument::setBaselinePolicies),
            rule(Title::getPolicyAssociation, ContentDocument::setPolicyAssociation),

            // are these fields below still needed?
            rule(Title::getMaterialId, ContentDocument::setMaterialIds),
            rule(Title::getBaseMaterialId, ContentDocument::setBaseMaterialId),
            rule(Title::getLookbackTitle, ContentDocument::setLookback),
            rule(Title::getCategories, ContentDocument::setCategories)

    );

    public static Function<List<Title>, List<ContentDocument>> mapContent(final YawlLogger logger) {
        return  -> .stream().map(mapTitle(logger)).flatMap(r -> r.map(Collections::singletonList)
                .orElse(Collections.emptyList()).stream()).collect(Collectors.toList());
    }

    public static Function<Title, Optional<ContentDocument>> mapTitle(final YawlLogger logger) {
        return  -> Try.of(() -> mapTitle(logger, ))
                .onFailure(error -> logger.error(LogCode.CMS_PARSING_DATA_ERROR, new Exception(error)))
                .toJavaOptional().flatMap(Function.identity());
    }

    public static Optional<ContentDocument> mapTitle(final YawlLogger logger, final Title ) {
        if (.getTitleId() == null || .getTitleId().trim().isEmpty()) {
            logger.error(LogCode.CMS_INVALID_DATA_ERROR,
                    "message", "Empty resource ID in  title");
            return Optional.empty();
        } else if (.getDuration() != null && .getDuration() >= Integer.MAX_VALUE) {
            logger.error(LogCode.CMS_INVALID_DATA_ERROR, "resourceId", .getId(),
                    "message", "Wrong duration value: " + .getDuration());
            return Optional.empty();
        } else {
            return Optional.ofNullable(MAPPING.map(, new ContentDocument()));
        }
    }

}
 **/

public interface Mapping<S, T> extends BiFunction<T, S, Boolean> {

    interface Setter<A, T> extends BiConsumer<T, A> {
    }

    default T map(final S s, final T t) {
        apply(t, s);
        return t;
    }

    default Mapping<S, T> orElse(Mapping<S, T> orElse) {
        return (t, s) -> apply(t, s) ? true : orElse.apply(t, s);
    }

    static <S, T> Mapping<S, T> mapping(final Mapping<S, T>... rules) {
        return (t, s) -> {
            for (final Mapping<S, T> rule : rules)
                rule.apply(t, s);
            return true;
        };
    }

    static <A, T> Mapping<A, T> setter(final Setter<A, T> set) {
        return (t, s) -> {
            set.accept(t, s);
            return true;
        };
    }

    static <S, T, A> Mapping<S, T> set(final A a,
                                       final Setter<A, T> set) {
        return (t, s) -> setter(set).apply(t, a);
    }

    static <S, T, A> Mapping<S, T> rule(final Function<S, A> get,
                                        final Setter<A, T> set) {
        return rule(get, setter(set));
    }

    static <S, T, A> Mapping<S, T> rule(final Function<S, A> get,
                                        final Mapping<A, T> set) {
        return rule(get, Function.identity(), set);
    }

    static <S, T, A, B> Mapping<S, T> rule(final Function<S, A> get,
                                           final Function<A, B> map,
                                           final Setter<B, T> set) {
        return rule(get, map, setter(set));
    }

    static <S, T, A, B> Mapping<S, T> rule(final Function<S, A> get,
                                           final Function<A, B> map,
                                           final Mapping<B, T> set) {
        return (t, s) -> Option.of(get.apply(s))
                .flatMap(a -> Option.of(map.apply(a)))
                .map(b -> set.apply(t, b))
                .getOrElse(false);
    }

    static <S, T, A> Mapping<S, T> forFirst(final Function<S, List<A>> get,
                                            final Setter<A, T> set) {
        return forFirst(get, setter(set));
    }

    static <S, T, A> Mapping<S, T> forFirst(final Function<S, List<A>> get,
                                            final Mapping<A, T> set) {
        return rule(get, firstOf(Function.identity()), set);
    }


    static <A, B> Function<List<A>, B> firstOf(final Function<A, B> map) {
        return firstOf(a -> true, map);
    }

    static <A, B> Function<List<A>, B> firstOf(final Predicate<A> filter,
                                               final Function<A, B> map) {
        return list -> Option.of(list).map(Iterator::ofAll)
                .flatMap(al -> al.find(filter)).map(map).getOrNull();
    }

    static <A, B> Function<A, B> create(final Supplier<B> create,
                                        final Setter<A, B> init) {
        return create(create, setter(init));
    }

    static <A, B> Function<A, B> create(final Supplier<B> create,
                                        final Mapping<A, B> init) {
        return a -> Option.of(a).map(v -> {
            final B b = create.get();
            init.apply(b, v);
            return b;
        }).getOrNull();
    }

    static <A, B> Function<List<A>, List<B>> forAll(final Function<A, B> map) {
        return list -> Option.of(list).map(Iterator::ofAll)
                .map(al -> al.flatMap(a -> Option.of(map.apply(a))))
                .map(Iterator::toJavaList).getOrNull();
    }

    static <A, B> Function<List<A>, List<B>> flatMap(final Function<A, List<B>> flatMap) {
        return list -> Option.of(list).map(Iterator::ofAll)
                .map(al -> al.flatMap(a -> Option.of(flatMap.apply(a))
                        .map(Iterator::ofAll).iterator()
                        .flatMap(Function.identity())))
                .map(Iterator::toJavaList).getOrNull();
    }

}
